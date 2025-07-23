package com.ryan.webfluxcoroutinesexample.common.filter

import io.github.oshai.kotlinlogging.KotlinLogging
import org.reactivestreams.Publisher
import org.slf4j.MDC
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.util.*

class RequestLogWebFilter(
    private val serviceName: String
) : WebFilter {

    private val log = KotlinLogging.logger("[$serviceName]")

    private val excludedUrls = listOf(
        "/v3/api-docs/swagger-config",
        "/v3/api-docs",
        "/swagger-ui/index.html",
        "/swagger-ui/swagger-ui.css",
        "/swagger-ui/index.css",
        "/swagger-ui/swagger-ui-bundle.js",
        "/swagger-ui/swagger-initializer.js",
        "/swagger-ui/swagger-ui-standalone-preset.js",
        "/swagger-ui/favicon-32x32.png",
        "/favicon.ico"
    )

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request

        if (excludedUrls.any { request.uri.path.startsWith(it) }) {
            return chain.filter(exchange)
        }

        val traceId = UUID.randomUUID().toString().substring(0, 8)
        MDC.put("traceId", traceId)

        val reqBody = StringBuilder()
        val respBody = StringBuilder()

        val mutatedRequest = object : ServerHttpRequestDecorator(request) {
            override fun getBody(): Flux<DataBuffer> {
                return super.getBody().doOnNext { buffer ->
                    val bytes = ByteArray(buffer.readableByteCount())
                    buffer.read(bytes)
                    DataBufferUtils.release(buffer)
                    reqBody.append(String(bytes, StandardCharsets.UTF_8))
                }
            }
        }

        val mutatedResponse = object : ServerHttpResponseDecorator(exchange.response) {
            override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
                val flux = Flux.from(body).map { buffer ->
                    val bytes = ByteArray(buffer.readableByteCount())
                    buffer.read(bytes)
                    respBody.append(String(bytes, StandardCharsets.UTF_8))
                    val newBuffer = DefaultDataBufferFactory().wrap(bytes)
                    DataBufferUtils.release(buffer)
                    newBuffer
                }
                return super.writeWith(flux)
            }
        }

        val mutatedExchange = exchange.mutate()
            .request(mutatedRequest)
            .response(mutatedResponse)
            .build()

        return chain.filter(mutatedExchange)
            .doFinally {
                try {
                    val logMsg = buildString {
                        append("TraceId [$traceId] || ")
                        append("Request [${request.method?.toString()}] ${request.uri.path} ")
                        append("[Query]: ${request.uri.query ?: ""} ")
                        if (request.method.name() != "GET") {
                            append("[Body]: ${reqBody.toString().replace("\n", "")} ")
                        }
                        append("|| Response [Status]: ${exchange.response.statusCode?.value() ?: "-"} ")
                        append("[Body]: ${respBody} || ")
                        append("Headers: ${request.headers}")
                    }
                    log.info { logMsg }
                } finally {
                    MDC.remove("traceId")
                }
            }
    }
}

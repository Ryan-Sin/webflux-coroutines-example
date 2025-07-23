package com.ryan.webfluxcoroutinesexample.common.config

import com.ryan.webfluxcoroutinesexample.common.constant.SecurityInfo
import com.ryan.webfluxcoroutinesexample.common.constant.SecurityMethod
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.*
import java.lang.reflect.ParameterizedType
import java.time.Instant

@Configuration
class SwaggerConfig {

	@Bean
	fun openAPI(): OpenAPI {
		return OpenAPI()
			.info(
				Info()
					.title("주문 시스템 API")
					.description("주문 시스템 API")
					.version("1.0.0")
			)
			.components(
				Components()
					.addSecuritySchemes(
						"bearerAuth",
						SecurityScheme()
							.type(SecurityScheme.Type.HTTP)
							.scheme("bearer")
							.bearerFormat("JWT")
					)
			)
			.addSecurityItem(
				SecurityRequirement()
					.addList("bearerAuth")
			)
	}

	@Bean
	fun customizeOperations(): OperationCustomizer {
		return OperationCustomizer { operation, handlerMethod ->
			val controllerPath = handlerMethod.method.declaringClass.annotations
				.filterIsInstance<RequestMapping>()
				.firstOrNull()?.value?.firstOrNull() ?: ""

			val methodAnnotation = handlerMethod.method.annotations
				.firstOrNull { it.annotationClass.simpleName?.endsWith("Mapping") == true }

			val methodPath = when (val annotation = methodAnnotation) {
				is RequestMapping -> annotation.value.firstOrNull()
				is GetMapping -> annotation.value.firstOrNull()
				is PostMapping -> annotation.value.firstOrNull()
				is PutMapping -> annotation.value.firstOrNull()
				is DeleteMapping -> annotation.value.firstOrNull()
				is PatchMapping -> annotation.value.firstOrNull()
				else -> null
			} ?: ""

			val fullPath = when {
				controllerPath.isNotEmpty() && methodPath.isNotEmpty() -> {
					val cleanControllerPath = controllerPath.trimStart('/')
					val cleanMethodPath = methodPath.trimStart('/')
					if (cleanMethodPath.startsWith(cleanControllerPath)) {
						"/$cleanMethodPath"
					} else {
						"/$cleanControllerPath/$cleanMethodPath"
					}
				}
				controllerPath.isNotEmpty() -> "/$controllerPath"
				methodPath.isNotEmpty() -> "/$methodPath"
				else -> ""
			}.replace("//", "/")

			val currentHttpMethod = when (methodAnnotation) {
				is GetMapping -> SecurityMethod.GET
				is PostMapping -> SecurityMethod.POST
				is PutMapping -> SecurityMethod.PUT
				is PatchMapping -> SecurityMethod.PATCH
				is DeleteMapping -> SecurityMethod.DELETE
				is RequestMapping -> {
					when {
						methodAnnotation.method.isEmpty() -> SecurityMethod.ALL
						methodAnnotation.method.contains(RequestMethod.GET) -> SecurityMethod.GET
						methodAnnotation.method.contains(RequestMethod.POST) -> SecurityMethod.POST
						methodAnnotation.method.contains(RequestMethod.PUT) -> SecurityMethod.PUT
						methodAnnotation.method.contains(RequestMethod.PATCH) -> SecurityMethod.PATCH
						methodAnnotation.method.contains(RequestMethod.DELETE) -> SecurityMethod.DELETE
						else -> null
					}
				}
				else -> null
			}

			val isPublicPath = SecurityInfo.PUBLIC_PATHS.entries.any { (method, paths) ->
				paths.any { pattern ->
					val regex = pattern
						.replace("/**", "/.*")
						.replace("*", "[^/]*")
						.replace("?", ".")
						.replace("{", "\\{")
						.replace("}", "\\}")
						.let { "^$it$" }

					val pathMatches = Regex(regex).matches(fullPath)

					when {
						method == SecurityMethod.ALL -> pathMatches
						currentHttpMethod != null -> pathMatches && (method == currentHttpMethod || method == SecurityMethod.ALL)
						else -> false
					}
				}
			}

			operation.security = if (isPublicPath) {
				emptyList()
			} else {
				listOf(SecurityRequirement().addList("bearerAuth"))
			}

			operation.responses.forEach { (statusCode, response) ->
				val returnType = handlerMethod.returnType
				val genericReturnType = handlerMethod.method.genericReturnType
				val isBinaryResponse = returnType == ByteArray::class.java ||
						(genericReturnType is ParameterizedType &&
								genericReturnType.actualTypeArguments.isNotEmpty() &&
								genericReturnType.actualTypeArguments[0] == ByteArray::class.java)

				if (isBinaryResponse) {
					return@OperationCustomizer operation
				}

				if (statusCode == "200" || statusCode == "201") {
					this.wrapperSuccessResponseBody(response)
				} else {
					this.wrapperErrorResponseBody(response)
				}
			}

			operation
		}
	}

	private fun wrapperSuccessResponseBody(response: ApiResponse) {
		response.content?.forEach { mediaTypeKey: String?, mediaType: MediaType ->
			val wrapperSchema: Schema<*> = Schema<Any>()

			if (mediaType.schema.`$ref`.equals("#/components/schemas/ApiResponseDtoUnit")) return@forEach

			wrapperSchema.addProperty(
				"timeStamp", Schema<Any>().type("string")
					.title("응답 시간")
					.format("date-time")
					.example(Instant.now().toString())
			)
			wrapperSchema.addProperty("data", mediaType.schema)

			mediaType.schema = wrapperSchema
		}
	}

	private fun wrapperErrorResponseBody(response: ApiResponse) {
		response.content?.forEach { mediaTypeKey: String?, mediaType: MediaType ->
			val wrapperSchema: Schema<*> = Schema<Any>()
			wrapperSchema.addProperty(
				"timeStamp", Schema<Any>().type("string").format("date-time")
					.title("응답 시간")
					.example(Instant.now().toString())
			)
			wrapperSchema.addProperty(
				"message", Schema<Any>().type("string")
					.title("에러 메시지")
					.example(mediaType.schema.default)
			)

			mediaType.schema = wrapperSchema
		}
	}
}

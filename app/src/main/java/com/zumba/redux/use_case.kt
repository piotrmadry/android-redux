package com.zumba.redux

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit

abstract class UseCase<in Params> {
    operator fun invoke(
        parameters: Params,
        timeoutMs: Long = defaultTimeoutMs
    ): Flow<Either<BasicError, Unit>> {
        return flow {
            withTimeout(timeoutMs) {
                doWork(parameters)
                emit(Either.Right(Unit) as Either<BasicError, Unit>)
            }
        }.catch { error ->
            emit(Either.Left(error.toBasicError()))
        }
    }

    suspend fun executeSync(parameters: Params) = doWork(parameters)

    protected abstract suspend fun doWork(parameters: Params)

    companion object {
        private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(5)
    }
}



abstract class ResultUseCase<in Params, out Result> {

    operator fun invoke(parameters: Params): Flow<Either<BasicError, Result>> {
        return flow { emit(Either.Right(doWork(parameters)) as Either<BasicError, Result>) }
            .catch { error -> emit(Either.Left(error.toBasicError())) }
    }

    suspend fun executeSync(parameters: Params): Result = doWork(parameters)

    protected abstract suspend fun doWork(parameters: Params): Result
}

@Suppress("USELESS_CAST")
abstract class FlowUseCase<in Params, out Result> {
    suspend operator fun invoke(parameters: Params): Flow<Either<BasicError, Result>> {
        return execute(parameters)
            .map { result -> Either.Right(result) as Either<BasicError, Result> }
            .catch { error -> emit(Either.Left(error.toBasicError())) }
    }

    protected abstract fun execute(parameters: Params): Flow<Result>
}

suspend operator fun UseCase<Unit>.invoke() = invoke(Unit)
suspend operator fun <Result> FlowUseCase<Unit, Result>.invoke() = invoke(Unit)
suspend operator fun <Result> ResultUseCase<Unit, Result>.invoke() = invoke(Unit)

sealed class BasicError {
    object NoNetwork : BasicError()

    object Unauthorized : BasicError()

    object Timeout : BasicError()

    object Unknown : BasicError() {

        data class ErrorUnknown(val throwable: Throwable) : BasicError() {
            override fun toString(): String = throwable.toString()
        }

        sealed class BaseHttpError : BasicError() {
            abstract val code: Int
            abstract val body: String

            data class HttpError(override val code: Int, override val body: String) :
                BaseHttpError()
        }
    }
}
fun Throwable.toBasicError(): BasicError = BasicError.Unknown.ErrorUnknown(this)
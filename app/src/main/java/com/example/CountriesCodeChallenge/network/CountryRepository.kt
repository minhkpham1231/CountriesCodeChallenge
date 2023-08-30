package com.example.CountriesCodeChallenge.network

import com.example.CountriesCodeChallenge.util.FailureResponse
import com.example.CountriesCodeChallenge.util.NullResponseException
import com.example.CountriesCodeChallenge.util.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CountryRepository {
    fun getCountry(): Flow<State>
}

class CountryRepositoryImpl(
    private val countryService: CountryService = CountryService.countryService
) : CountryRepository {

    override fun getCountry(): Flow<State> = flow {
        emit(State.LOADING())

        try {
            val response = countryService.getCountry()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(State.SUCCESS(it))
                } ?: throw NullResponseException("Null respond")
            } else {
                throw FailureResponse("Country response failed")
            }
        } catch (e: Exception) {
            emit(State.ERROR(e))
        }
    }
}
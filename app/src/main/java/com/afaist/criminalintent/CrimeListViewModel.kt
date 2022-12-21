package com.afaist.criminalintent

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {
    /**
     * Crimes
     */
    private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData = crimeRepository.getCrimes()
}
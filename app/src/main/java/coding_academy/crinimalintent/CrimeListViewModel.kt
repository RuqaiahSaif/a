package coding_academy.crinimalintent

import androidx.lifecycle.ViewModel
import java.io.File

class CrimeListViewModel: ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    var crimeListLiveData = crimeRepository.getCrimes()
    fun addCrime(crime: Crime) {
        crimeRepository.addCrime(crime)
    }
    fun getPhotoFile(crime: Crime): File {
        return crimeRepository!!.getPhotoFile(crime)
    }

}
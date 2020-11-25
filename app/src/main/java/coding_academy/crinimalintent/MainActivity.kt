package coding_academy.crinimalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.util.*
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),CrimeListFragment.CallBacks  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container,CrimeListFragment.newInstance())
                .commit()

        }


    override fun onItemSelected(crimeId: UUID) {
        val fragment = CrimeFragment.newInstance(crimeId)
        val fm = supportFragmentManager
        fm?.beginTransaction()?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)
            .commit()
    }


}
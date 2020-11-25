package coding_academy.crinimalintent

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.text.DateFormat
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment: Fragment() {
    private lateinit var photoFile: File
    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter:  CrimeAdapter? =  CrimeAdapter(emptyList())
    private lateinit var add: Button
    private lateinit var no_crime: TextView
    private lateinit var crime: Crime
    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }
    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeDetailViewModel::class.java)
    }
    var callBacks:CallBacks?=null
interface CallBacks{
    fun onItemSelected(crimeId: UUID)
}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callBacks=context as CallBacks?

    }

    override fun onDetach() {
        super.onDetach()
        callBacks=null
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.crime_list_fragment, container, false)
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view)
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter = adapter
        add = view.findViewById(R.id.add1) as Button
        no_crime = view.findViewById(R.id.no_crime) as TextView


        return view

    }
    private fun updateUI(crimes: List<Crime>) {

        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter

        val adapterTemp = crimeRecyclerView.adapter as CrimeAdapter
        adapterTemp.submitList(crimes)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes ${crimes.size}")

                    updateUI(crimes)
                }
                })
            }


                public abstract inner class BaseViewHolder(itemView: View) :
                    RecyclerView.ViewHolder(itemView) {
                    abstract fun bind(crime: Crime)
                }

                private inner class CrimeHolder(view: View) : BaseViewHolder(view),
                    View.OnClickListener {
                    private lateinit var crime: Crime
                    val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
                    val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
                    private var crimeImageView: ImageView = itemView.findViewById(R.id.ImageView)
                    private val solvedImageView1: ImageView = itemView.findViewById(R.id.crime_solved)

                    init {
                        itemView.setOnClickListener(this)
                    }

                    override fun bind(crime: Crime) {
                        this.crime = crime
                        titleTextView.text = this.crime.title

                        dateTextView.text = DateFormat.getTimeInstance(DateFormat.FULL).format(this.crime.date).toString()
                        
                        solvedImageView1.visibility = if (crime.isSolved) {
                            View.VISIBLE
                        } else {

                            image_crime(crimeImageView , crime)
                            View.GONE
                        }

                    }

                    override fun onClick(v: View) {


                        callBacks?.onItemSelected(crime.id)
                    }
                }

                private inner class SeriousCrimeHolder(view: View) : BaseViewHolder(view),
                    View.OnClickListener {
                    private lateinit var crime: Crime
                    val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
                    val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
                    private val solvedImageView: ImageView =
                        itemView.findViewById(R.id.crime_solved)
                    private val police: Button = itemView.findViewById(R.id.button)

                    init {
                        itemView.setOnClickListener(this)
                    }

                    override fun bind(crime: Crime) {
                        this.crime = crime
                        titleTextView.text = this.crime.title
                        dateTextView.text =
                            DateFormat.getTimeInstance(DateFormat.FULL).format(this.crime.date)
                                .toString()
                        solvedImageView.visibility = if (crime.isSolved) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }

                    }

                    override fun onClick(v: View) {
                        callBacks?.onItemSelected(crime.id)
                    }
                }

                private inner class CrimeAdapter(var crimes: List<Crime>) :
                    androidx.recyclerview.widget.ListAdapter<Crime , RecyclerView.ViewHolder>(CrimeDiffUtil()) {

                    override fun onCreateViewHolder(
                        parent: ViewGroup,
                        viewType: Int
                    ): BaseViewHolder {
                        if (viewType == 1) {
                            val view = layoutInflater.inflate(R.layout.serious_crime, parent, false)
                            return SeriousCrimeHolder(view)
                        } else {
                            val view =
                                layoutInflater.inflate(R.layout.list_item_crime, parent, false)
                            return CrimeHolder(view)

                        }
                    }



                   override fun getItemViewType(position: Int): Int {
                    val type= when(getItem(position).isSolved){
              true ->1
                        else ->0
                    }

                 return type
                    }
                    override fun getItemCount(): Int {
                        if(crimes.size<=0){
                            add.visibility = View.VISIBLE
                            no_crime.visibility = View.VISIBLE
                        }else{
                            add.visibility = View.GONE
                            no_crime.visibility = View.GONE
                        }
                        return crimes.size

                    }

                    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                        val crime = getItem(position)
                        when(getItem(position).isSolved){
                            true->(holder as SeriousCrimeHolder).bind(crime)
                                false->(holder as CrimeHolder).bind(crime)
                        }
                    }


                }

    class CrimeDiffUtil: DiffUtil.ItemCallback<Crime>(){
        override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean {
            return (
                    oldItem.id == newItem.id )
        }

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                crimeListViewModel.addCrime(crime)
                callBacks?.onItemSelected(crime.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        add.setOnClickListener{

            val crime = Crime()
            crimeListViewModel.addCrime(crime)
            callBacks?.onItemSelected(crime.id)


        }


    }
    fun image_crime(crimeImageView: ImageView, crime: Crime) {
        photoFile = crimeListViewModel.getPhotoFile(crime)
        if (photoFile.exists()) {
            var pictureUtils = PictureUtils()
            val bitmap = pictureUtils.getScaledBitmap(
                photoFile.path ,
                requireActivity()
            )
            crimeImageView.setImageBitmap(bitmap)
        } else
            crimeImageView.setImageDrawable(null)

    }

}


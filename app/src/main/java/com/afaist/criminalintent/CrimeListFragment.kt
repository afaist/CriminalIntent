package com.afaist.criminalintent

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.util.*

private const val TAG = "CrimeListFragment"

/**
 * Crime list fragment
 *
 * @constructor Create empty Crime list fragment
 */
class CrimeListFragment : Fragment() {
    /**
     * Callbacks
     *
     * @constructor Create empty Callbacks
     */
    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    /**
     * Callbacks
     */
    private var callbacks: Callbacks? = null

    /**
     * Crime recycler view
     */
    private lateinit var crimeRecyclerView: RecyclerView

    /**
     * Adapter
     */
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())

    /**
     * Data model
     */
    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this)[CrimeListViewModel::class.java]
    }

    /**
     * On attach
     *
     * @param context
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    /**
     * Companion
     *
     * @constructor Create empty Companion
     */
    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }

    /**
     * On create view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView =
            view.findViewById(R.id.crime_recycler_view)
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter = adapter
        return view
    }

    /**
     * On view created
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner
        ) { crimes ->
            crimes?.let {
                Log.i(TAG, "Cot crimes ${crimes.size}")
                updateUI(crimes)
            }
        }
    }

    /**
     * On detach
     *
     */
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    /**
     * Update u i
     *
     * @param crimes
     */
    private fun updateUI(crimes: List<Crime>) {
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }

    /**
     * Crime holder
     *
     * @constructor
     *
     * @param view
     */
    private inner class CrimeHolder(view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var crime: Crime
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView = itemView.findViewById<ImageView>(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        /**
         * Bind
         *
         * @param crime
         */
        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title

            dateTextView.text = DateFormat.getDateInstance(DateFormat.FULL).format(crime.date)
            solvedImageView.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        /**
         * On click
         *
         * @param v
         */
        override fun onClick(v: View?) {
            callbacks?.onCrimeSelected(crime.id)
        }
    }

    /**
     * Crime adapter
     *
     * @property crimes
     * @constructor Create empty Crime adapter
     */
    private inner class CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<CrimeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)

        }

        /**
         * On bind view holder
         *
         * @param holder
         * @param position
         */
        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

        /**
         * Get item count
         *
         */
        override fun getItemCount() = crimes.size
    }
}
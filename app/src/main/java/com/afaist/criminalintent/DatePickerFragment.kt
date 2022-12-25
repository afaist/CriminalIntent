package com.afaist.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_DATE = "date"

@Suppress("DEPRECATION")
class DatePickerFragment : DialogFragment() {
    /**
     * Callbacks
     *
     * @constructor Create empty Callbacks
     */
    interface Callbacks {
        fun onDateSelected(date: Date)
    }

    /**
     * On create dialog
     *
     * @param savedInstanceState
     * @return
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dateListener = DatePickerDialog.OnDateSetListener {
                _: DatePicker, year: Int, month: Int, day: Int ->
            val resultDate: Date = android.icu.util.GregorianCalendar(year, month, day).time
            targetFragment?.let { fragment ->
                (fragment as Callbacks).onDateSelected(resultDate)
            }
        }
        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(ARG_DATE, Date::class.java)
        } else {
            arguments?.getSerializable(ARG_DATE) as Date
        }
        val calendar = Calendar.getInstance()
        calendar.time = date
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    /**
     * Companion
     *
     * @constructor Create empty Companion
     */
    companion object {
        /**
         * New instance
         *
         * @param date
         * @return
         */
        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
            }
            return DatePickerFragment().apply {
                arguments = args
            }
        }
    }
}
package com.afaist.criminalintent

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.util.*

private const val ARG_TIME = "time"
private const val ARG_REQUEST_CODE = "requestCode"
private const val RESULT_TIME_KEY = "resultCode"

@Suppress("DEPRECATION")
class TimePickerFragment : DialogFragment() {

    interface Callbacks {
        fun onTimeSelected(date: Date)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(ARG_TIME, Date::class.java)
        } else {
            arguments?.getSerializable(ARG_TIME) as Date
        }

        val calendar = Calendar.getInstance()

        calendar.time = date
        val initialHour = calendar.get(Calendar.HOUR)
        val initialMinute = calendar.get(Calendar.MINUTE)

        val timeListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                val resultDate: Date = calendar.time
                // create our result Bundle
                val result = Bundle().apply {
                    putSerializable(RESULT_TIME_KEY, resultDate)
                }
                val resultRequestCode = requireArguments().getString(ARG_REQUEST_CODE, "")
                setFragmentResult(resultRequestCode, result)
            }


        return TimePickerDialog(
            requireContext(),
            timeListener,
            initialHour,
            initialMinute,
            true
        )
    }

    companion object {
        fun newInstance(date: Date): TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TIME, date)
            }
            return TimePickerFragment().apply {
                arguments = args
            }
        }

    }
}
package com.ikurek.pwr.jsos


import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.ikurek.pwr.R
import kotlinx.android.synthetic.main.fragment_jsos.view.*


/**
 * A simple [Fragment] subclass.
 */
class JSOSFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_jsos, container, false)

        setupUI(view)
        getJSOSStatus(view)

        return view
    }

    /**
     * Basic UI setup
     * @param view Root view required to do UI operations
     */
    private fun setupUI(view: View) {

        view.progress_bar_jsos_fragment.isIndeterminate = true
        view.refersh_button_jsos_fargment.setOnClickListener {
            getJSOSStatus(view)
        }
    }

    /**
     * Sends HTTP GET request to czyjsosdziala.pl
     * Receives true/false parameter defining current JSOS status
     * Usually it's false
     * Updates the UI with received data
     * @param view Root view used to update layout elements
     */
    private fun getJSOSStatus(view: View) {

        view.current_status_jsos_fragment.text = ""
        view.progress_bar_jsos_fragment.visibility = View.VISIBLE

        Fuel.get("http://czyjsosdziala.pl/api").responseString { request, response, result ->

            result.fold({ data ->

                view.progress_bar_jsos_fragment.visibility = View.GONE
                view.current_status_jsos_fragment.text = data

            }, { error ->

                Toast.makeText(activity, error.localizedMessage.toString(), Toast.LENGTH_LONG).show()

            })
        }

    }

}

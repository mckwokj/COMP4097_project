package edu.hkbu.comp.comp4097.comp4097project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import edu.hkbu.comp.comp4097.comp4097project.data.AppDatabase
import edu.hkbu.comp.comp4097.comp4097project.data.Place
import edu.hkbu.comp.comp4097.comp4097project.data.PlaceInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.round

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)


        val xid = arguments?.getString("xid", "")
        var place: PlaceInfo? = null

        val job = CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getInstance(requireContext()).placeDao()

            if (xid != null) {
                place = dao.findPlaceByXid(xid)
                Log.d("DetailFragment", place.toString())
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            job.join()
            val imageView: ImageView = view.findViewById(R.id.detailImageView)
            val placeTextView: TextView = view.findViewById(R.id.detailPlaceText)
            val descTextView: TextView = view.findViewById(R.id.detailDescText)
            val districtTextView: TextView = view.findViewById(R.id.detailDistrictText)

            val image_URL = place?.image_URL
            val attractionName = place?.name
            val districtText = place?.district
            val description = place?.description
            val roundedDistance = place?.dist?.times(100)?.let { round(it) / 100 }.toString()

            Picasso.get().load(image_URL).into(imageView)
            placeTextView.text = attractionName
            districtTextView.text = "District:${districtText}\nApproximate distance: " +
                    "${roundedDistance}m"
            descTextView.text = "Description:${description}"

//            if (image_URL != null) {
//                Log.d("DetailFragment", image_URL)
//            }
//            if (attractionName != null) {
//                Log.d("DetailFragment", attractionName)
//            }
//            if (districtText != null) {
//                Log.d("DetailFragment", districtText)
//            }
//            if (description != null) {
//                Log.d("DetailFragment", description)
//            }

//        if (attractionName != null) {
//            Log.d("DetailFragment", attractionName)
//        }
        }

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
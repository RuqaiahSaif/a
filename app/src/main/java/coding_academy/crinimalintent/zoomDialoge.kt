package coding_academy.crinimalintent

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import java.io.File

private val FILE_ARG = "photo"
class zoomDialoge: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
        val view = activity?.layoutInflater?.inflate(R.layout.zoom_dailoge, null)
        val imgeView = view?.findViewById(R.id.zoomImage) as ImageView
        val imgeFile = arguments?.getSerializable(FILE_ARG) as File
        if (imgeFile.exists()) {
            var pictureUtils = PictureUtils()
            val bitmap = pictureUtils.getScaledBitmap(
                imgeFile.path ,
                requireActivity()
            )
            imgeView.setImageBitmap(bitmap)
        } else
            imgeView.setImageDrawable(null)
        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setTitle("Image zooming")
            .setNegativeButton("back") { dialog , _ ->
                dialog.cancel()

            }.create()
    }
    companion object {
        fun newInstance(photoFile: File): zoomDialoge {
            val args = Bundle().apply {
                putSerializable(FILE_ARG , photoFile)
            }
            return zoomDialoge().apply {
                arguments = args
            }

        }
    }
}



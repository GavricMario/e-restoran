package hr.fer.grupa.erestoran

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window.FEATURE_NO_TITLE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.custom_menu_dialog.*


class MenuDialog(context: Context) : Dialog(context, android.R.style.Theme_Light) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(FEATURE_NO_TITLE)
        setContentView(R.layout.custom_menu_dialog)

        val listItem = context.resources.getStringArray(R.array.array_settings)
        val adapter = CircularAdapter(context, listItem)
        listView.layoutManager = LinearLayoutManager(context)

        listView.adapter = adapter
        listView.scrollToPosition(adapter.MIDDLE)


        listView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val firstPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val lastPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                (recyclerView.adapter as CircularAdapter).firstVisiblePosition = firstPosition
                (recyclerView.adapter as CircularAdapter).lastVisiblePosition = lastPosition

                (recyclerView.adapter as CircularAdapter).notifyDataSetChanged()
            }
        })

        menuButton.setOnClickListener {
            this.dismiss()
        }

        menuButton2.setOnClickListener {
            this.dismiss()
        }
    }
}
package hr.fer.grupa.erestoran

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window.FEATURE_NO_TITLE
import kotlinx.android.synthetic.main.custom_menu_dialog.*


class MenuDialog(context: Context) : Dialog(context, android.R.style.Theme_Light), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(FEATURE_NO_TITLE)
        setContentView(R.layout.custom_menu_dialog)

        item1.setOnClickListener(this)
        item2.setOnClickListener(this)
        item3.setOnClickListener(this)
        item4.setOnClickListener(this)
        item5.setOnClickListener(this)
        item6.setOnClickListener(this)
        item7.setOnClickListener(this)
        menuButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.item1-> this.dismiss()
            R.id.item2-> this.dismiss()
            R.id.item3-> this.dismiss()
            R.id.item4-> this.dismiss()
            R.id.item5-> this.dismiss()
            R.id.item6-> this.dismiss()
            R.id.item7-> this.dismiss()
            R.id.menuButton -> this.dismiss()
        }
        this.dismiss()
    }
}
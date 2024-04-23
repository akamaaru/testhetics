package com.example.testhetics.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.testhetics.R
import com.google.firebase.auth.FirebaseAuth


open class DefaultActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.aboutOption) {
            val dialog = AlertDialog.Builder(this)
            try {
                val aboutMessage =
                    """$title версия ${packageManager.getPackageInfo(packageName, 0).versionName} 
                        |
                        |Курсовой проект "Андроид-приложение для создания и прохождения квизов и тестов "Тестетика" 
                        |
                        |Автор: Беренштейн Аркадий Игоревич БПИ225""".trimMargin()
                dialog.setMessage(aboutMessage)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            dialog.setTitle(R.string.aboutOptionText)
            dialog.setNeutralButton("OK") { dialogInterface: DialogInterface,
                                            _: Int ->
                dialogInterface.dismiss()
            }

            dialog.setIcon(R.drawable.logo_background)
            val alertDialog = dialog.create()
            alertDialog.show()
        } else if (id == R.id.logoutOption) {
            val authentication = FirebaseAuth.getInstance()
            authentication.signOut()
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
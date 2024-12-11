package com.example.androidecommerceapp.ui.shareDetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.Manifest
import android.annotation.SuppressLint
import com.example.androidecommerceapp.dataModel.Contact
import com.example.androidecommerceapp.databinding.ActivityShareBinding
import com.example.androidecommerceapp.ui.adapter.ContactsAdapter


class ShareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShareBinding
    private val REQUEST_CODE_READ_CONTACTS = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check and request permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CODE_READ_CONTACTS
            )
        } else {
            // Load contacts if permission is granted
            loadContacts()
        }
    }

    // Request permission result callback
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_READ_CONTACTS && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            loadContacts()
        } else {
            Toast.makeText(this, "Permission denied to read contacts", Toast.LENGTH_SHORT).show()
        }
    }

    // Load contacts from ContentProvider
    private fun loadContacts() {
        val contacts = getContacts(this)
        val adapter = ContactsAdapter(applicationContext, contacts)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    // Get contacts from ContentProvider
    private fun getContacts(context: ShareActivity): List<Contact> {
        val contactList = mutableListOf<Contact>()
        val contentResolver: ContentResolver = context.contentResolver

        val uri = ContactsContract.Contacts.CONTENT_URI

        val projection = arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY // Only the name
        )

        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)


            // Check if the column index is valid (>= 0) before accessing it
            if (nameIndex >= 0) {
                while (it.moveToNext()) {
                    val contactName = it.getString(nameIndex)
                    contactList.add(Contact(contactName))
                }
            } else {
                Toast.makeText(this, "Error: Column index is invalid", Toast.LENGTH_SHORT).show()
            }
        }

        return contactList
    }

    // Get phone number from a contact
    @SuppressLint("Range")
    private fun getPhoneNumber(contentResolver: ContentResolver, contactId: String): String {
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val phoneSelection = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
        val phoneCursor = contentResolver.query(
            phoneUri,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            phoneSelection,
            arrayOf(contactId),
            null
        )

        phoneCursor?.use {
            if (it.moveToFirst()) {
                return it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
        }

        return "No Phone Number"
    }
}


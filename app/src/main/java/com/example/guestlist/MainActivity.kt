package com.example.guestlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

// create variable to save data
const val LAST_GUEST_NAME_KEY = "last-guest-name-bundle-key"

// : (extends) -- gives us access to a pre-written library/framework
// so we don't have to rewrite a set of code for basic functions of an android app
// everytime we create a new program
// classes can extend classes, and those classes can extend that next subclass-- and so on
// to give different types of functionality for newer versions of android, etc.
class MainActivity : AppCompatActivity() {

    private lateinit var addGuestButton: Button
    private lateinit var newGuestEditText: EditText
    private lateinit var guestList: TextView
    private lateinit var lastGuestAdded: TextView
    private lateinit var clearGuestListButton: Button

    // create data structure to hold list of guest names-- mutable, so we can alter it
    //val guestNames = mutableListOf<String>()

    private val guestListViewModel: GuestListViewModel by lazy {
        // -- this will be prepared, but won't run until an activity runs that needs to use it
        // -- lambda won't be called until guestListViewModel is used
        ViewModelProvider(this).get(GuestListViewModel::class.java)
    }

    //alternate syntax for creating this list
    //var guestNames: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addGuestButton = findViewById(R.id.add_guest_button)
        newGuestEditText = findViewById(R.id.new_guest_input)
        guestList = findViewById(R.id.list_of_guests)
        lastGuestAdded = findViewById(R.id.last_guest_added)
        // initialize button for clearing guest list
        clearGuestListButton = findViewById(R.id.clear_guests_button)

        addGuestButton.setOnClickListener {
            addNewGuest()
        }

        clearGuestListButton.setOnClickListener {
            guestListViewModel.deleteAllGuests()
            updateGuestList()
        }

        // in onCreate, so when a new session is started, we can restore the data
        // that we saved using onSaveInstanceState (crash/shutdown/rotation)
        val savedLastGuestMessage = savedInstanceState?.getString(LAST_GUEST_NAME_KEY)
        lastGuestAdded.text = savedLastGuestMessage

        updateGuestList()

    }

    // create new function for saving data
    // android system automatically calls this function, we don't need to
    // called on shutdown, rotation, battery loss, etc.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_GUEST_NAME_KEY, lastGuestAdded.text.toString())
    }


    private fun addNewGuest() {
        // retrieve name entered
        val newGuestName = newGuestEditText.text.toString()
        if (newGuestName.isNotBlank()) {
            // add new guest name to guestNames list
            //guestNames.add(newGuestName)
              guestListViewModel.addGuest(newGuestName)
            // create new function to put together and display guest list with new additions
            updateGuestList()
            // clear the text in the newGuestEditText (user input box)
            newGuestEditText.text.clear()
            // set the text for the last guest added text view at the bottom of the screen
            // to the message stating the name of the last added guest
            lastGuestAdded.text = getString(R.string.last_guest_message, newGuestName)

        } else {

        }
    }

    private fun updateGuestList() {
        val guests = guestListViewModel.getSortedGuestNames()
        val guestDisplay = guests.joinToString(separator = "\n")
        guestList.text = guestDisplay
    }


}
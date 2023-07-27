package com.tugcearas.noteapp_upschool.ui.note

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.tugcearas.noteapp_upschool.data.model.NoteModel
import com.tugcearas.noteapp_upschool.data.source.NoteDatabase
import com.tugcearas.noteapp_upschool.databinding.AlertDialogBinding
import com.tugcearas.noteapp_upschool.databinding.FragmentNotesScreenBinding
import com.tugcearas.noteapp_upschool.ui.adapter.NoteAdapter
import com.tugcearas.noteapp_upschool.util.click
import com.tugcearas.noteapp_upschool.util.showDatePicker
import com.tugcearas.noteapp_upschool.util.showTimePicker
import com.tugcearas.noteapp_upschool.util.toastMessage
import java.util.Calendar

class NotesScreen : Fragment() {

    private lateinit var binding: FragmentNotesScreenBinding
    private val noteAdapter by lazy { NoteAdapter(onItemClick = ::onClickNoteItem) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesScreenBinding.inflate(inflater,container,false)

        with(binding){
            floatingBtn.click {
                showDialog()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAdapter()
        checkBoxClick()
    }

    private fun createAdapter() {
        binding.apply {
            val getList = NoteDatabase.getHomeNotes()
            rvHome.adapter = noteAdapter
            noteAdapter.updateList(getList)
        }
    }

    private fun checkBoxClick() {
        noteAdapter.checkboxClick = { noteModel ->
            NoteDatabase.deleteNoteInHome(noteModel)
            noteModel.description?.let {
                NoteDatabase.addNoteInSaved(
                    noteModel.title,
                    it,
                    noteModel.date,
                    noteModel.saveType
                )
            }
            requireContext().toastMessage("Transferred note successfully to Saved!")
            createAdapter()
        }
    }

    private fun onClickNoteItem(note:NoteModel){
        val action = NotesScreenDirections.actionNotesScreenToDetailNoteScreen(note,true)
        findNavController().navigate(action)
    }

    private fun showDialog(){
        val builder = AlertDialog.Builder(requireContext())
        val binding = AlertDialogBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        val dialog = builder.create()

        val saveTypeList = listOf("Notes", "Saved")

        var selectedSaveType = ""
        var selectedDate = ""

        val saveTypeAdapter = ArrayAdapter(
            requireContext(), androidx.transition.R.layout.support_simple_spinner_dropdown_item, saveTypeList
        )

        with(binding) {

            val calendar = Calendar.getInstance()
            actSaveType.setAdapter(saveTypeAdapter)

            actSaveType.setOnItemClickListener { _, _, position, _ ->
                selectedSaveType = saveTypeList[position]
            }

            switchDate.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    showDatePicker(calendar) { year, month, day ->
                        showTimePicker(calendar) { hour, minute ->
                            selectedDate = "$day.$month.$year - $hour:$minute"
                            tvDate.text = "$day.$month.$year - $hour:$minute"
                            tvDate.visibility = View.VISIBLE
                        }
                    }
                }
            }

            btnAdd.setOnClickListener {
                val title = etTitle.text.toString()
                val desc = etDesc.text.toString()

                if (title.isNotEmpty() && desc.isNotEmpty() && selectedSaveType.isNotEmpty() && selectedSaveType == "Notes") {
                    NoteDatabase.addNoteInHome(title, desc, selectedDate, selectedSaveType)
                    requireContext().toastMessage("Successfully added in Notes!")
                    createAdapter()
                    dialog.dismiss()
                }
                else {
                    NoteDatabase.addNoteInSaved(title, desc, selectedDate, selectedSaveType)
                    requireContext().toastMessage("Successfully added in Saved!")
                    dialog.dismiss()
                }
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}
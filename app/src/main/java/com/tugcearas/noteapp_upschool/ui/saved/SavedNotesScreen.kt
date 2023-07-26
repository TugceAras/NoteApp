package com.tugcearas.noteapp_upschool.ui.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tugcearas.noteapp_upschool.data.model.NoteModel
import com.tugcearas.noteapp_upschool.data.source.NoteDatabase
import com.tugcearas.noteapp_upschool.databinding.FragmentSavedNotesScreenBinding
import com.tugcearas.noteapp_upschool.ui.adapter.NoteAdapter
import com.tugcearas.noteapp_upschool.util.toastMessage

class SavedNotesScreen : Fragment() {

    private lateinit var binding: FragmentSavedNotesScreenBinding
    private val noteAdapter by lazy { NoteAdapter(onItemClick = ::onClickNoteItem) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedNotesScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAdapter()
        checkBoxClick()
    }

    private fun createAdapter() {
        binding.apply {
            val getList = NoteDatabase.getSavedNotes()
            rvSaved.adapter = noteAdapter
            noteAdapter.updateList(getList)
        }
    }

    private fun checkBoxClick() {
        noteAdapter.checkboxClick = { noteModel ->
            NoteDatabase.deleteNoteInSaved(noteModel)
            noteModel.description?.let {
                NoteDatabase.addNoteInHome(
                    noteModel.title,
                    it,
                    noteModel.date,
                    noteModel.saveType
                )
            }
            requireContext().toastMessage("Deleted and transferred note successfully!")
            createAdapter()
        }
    }

    private fun onClickNoteItem(note: NoteModel){
        val action = SavedNotesScreenDirections.actionSavedNotesScreenToDetailNoteScreen(note, fromSaved = true)
        findNavController().navigate(action)
    }
}
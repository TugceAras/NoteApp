package com.tugcearas.noteapp_upschool.ui.detail

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tugcearas.noteapp_upschool.data.source.NoteDatabase
import com.tugcearas.noteapp_upschool.databinding.FragmentDetailNoteScreenBinding
import com.tugcearas.noteapp_upschool.util.click
import com.tugcearas.noteapp_upschool.util.toastMessage

class DetailNoteScreen : Fragment() {

    private lateinit var binding: FragmentDetailNoteScreenBinding
    private val args:DetailNoteScreenArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailNoteScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            tvDetailTitle.text = args.note.title
            tvDetailDescription.text = args.note.description
            tvDetailDate.text = args.note.date
        }
        clickDeleteButton()
        clickBackButton()
    }

    private fun clickDeleteButton(){
        binding.btnDetailDelete.click {
            AlertDialog.Builder(requireContext())
                .setTitle("Delete Note")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Yes"){_,_ ->
                    if (args.fromHome){
                        NoteDatabase.deleteNoteInHome(args.note)
                        requireContext().toastMessage("Deleted successfully in Notes!")
                    }
                    else{
                        NoteDatabase.deleteNoteInSaved(args.note)
                        requireContext().toastMessage("Deleted successfully in Saved!")
                    }
                }
                .setNegativeButton("No",null)
                .show()
        }
    }

    private fun clickBackButton(){
        binding.btnDetailBack.click {
            val action = DetailNoteScreenDirections.actionDetailNoteScreenToNotesScreen()
            findNavController().navigate(action)
        }
    }
}
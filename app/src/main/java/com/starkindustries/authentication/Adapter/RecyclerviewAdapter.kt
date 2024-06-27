package com.starkindustries.authentication.Adapter
import android.content.Context
import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.starkindustries.authentication.Model.Notes
import com.starkindustries.authentication.R
class RecyclerviewAdapter(var context_: Context,notesList_:ArrayList<Notes>,val itemClickListner:OnClickListner):RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder>()
{
    interface OnClickListner{
        fun onUpdateClicked(noteId:String)
        fun onDeleteClicked(noteId:String)
    }
    val context:Context
    val notesList:ArrayList<Notes>

    init{
        this.context=context_
        this.notesList=notesList_
    }
    inner class ViewHolder(var view:View):RecyclerView.ViewHolder(view)
    {
        val noteTitle:AppCompatTextView
        val noteThought:AppCompatTextView
        val updateButton:AppCompatButton
        val deleteButton:AppCompatButton
        init {
            noteTitle=view.findViewById(R.id.noteTitle)
            noteThought=view.findViewById(R.id.noteThought)
            updateButton=view.findViewById(R.id.updateButton)
            deleteButton=view.findViewById(R.id.deleteButton)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        var view = LayoutInflater.from(context).inflate(R.layout.note_row,parent,false)
        var viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTitle.text=notesList.get(position).title
        holder.noteThought.text=notesList.get(position).thought
        val note = notesList.get(position)
        holder.updateButton.setOnClickListener()
        {
            itemClickListner.onUpdateClicked(note.noteId)
        }
        holder.deleteButton.setOnClickListener {
            itemClickListner.onDeleteClicked(note.noteId)
        }
    }
}
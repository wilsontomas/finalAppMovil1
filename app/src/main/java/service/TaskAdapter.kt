package service

import Data.task_table
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.tvshowapp.R
import com.example.tvshowapp.databinding.TaskItemBinding
import com.squareup.picasso.Picasso
import model.taskModel
import model.tv_shows_model

class TaskAdapter(
    var tvshows:List<tv_shows_model>
): RecyclerView.Adapter<TaskAdapter.movieViewHolder>() {
    private lateinit var Id:Number;
    private lateinit var mListener:onItemClickListener
    interface onItemClickListener{
        fun itemClick(id:Number)
    }
    fun setOnItemClickListener(listener:onItemClickListener){
        mListener = listener;
    }
    inner class movieViewHolder(binding: TaskItemBinding,listener:onItemClickListener):RecyclerView.ViewHolder(binding.root){
        val binding = binding;
        init{
            binding.movieImageId.setOnClickListener {
                listener.itemClick(adapterPosition);
            }
    }
    }
    /*
    inner class TaskViewHolder(itemView:View,listener:onItemClickListener):RecyclerView.ViewHolder(itemView){
        init{
            itemView.setOnClickListener {
                listener.itemClick(adapterPosition);
            }
        }
    }*/

   // private lateinit var mListener:
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): movieViewHolder {
      /*  val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item,parent,false);
        return TaskViewHolder(view,mListener);*/
        //return movieViewHolder(TaskItemBinding.inflate(LayoutInflater.from(parent.context)),mListener);
        return movieViewHolder(TaskItemBinding.inflate(LayoutInflater.from(parent.context)),mListener);

    }

    override fun onBindViewHolder(holder: movieViewHolder, position: Int) {
        val TvShow = tvshows!![position];
        Id=TvShow.id;
        Picasso.get().load(TvShow.image_thumbnail_path)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .resize(80*2,100*2)
            .into(holder.binding.movieImageId);
        holder.binding.tituloMovieId.text=TvShow.name;
        holder.binding.paisMovieId.text=TvShow.country;
        holder.binding.yearMovieId.text=TvShow.start_date;


    }

    override fun getItemCount(): Int {
       return tvshows.size;
    }
}
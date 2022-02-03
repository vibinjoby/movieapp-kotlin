package com.movie.mvvm.ui.popular_movie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.movie.mvvm.R
import com.movie.mvvm.data.api.POSTER_BASE_URL
import com.movie.mvvm.data.repository.NetworkState
import com.movie.mvvm.data.vo.PopularMovie
import com.movie.mvvm.ui.single_movie_details.SingleMovie

class PopularMovieViewHolder(private val view: View,private val context: Context): RecyclerView.ViewHolder(view) {
    private val moviePoster = view.findViewById<ImageView>(R.id.cv_iv_movie_poster)
    private val movieTitle = view.findViewById<TextView>(R.id.cv_movie_title)
    private val movieReleaseDate = view.findViewById<TextView>(R.id.cv_movie_release_date)

    fun bind(popularMovie: PopularMovie?) {
        movieTitle.text = popularMovie?.title
        movieReleaseDate.text = popularMovie?.releaseDate

        val posterUrl = POSTER_BASE_URL + popularMovie?.posterPath
        Glide.with(view.context)
            .load(posterUrl)
            .into(moviePoster)

        view.setOnClickListener {
            val intent = Intent(context, SingleMovie::class.java)
            intent.putExtra("movie_id",popularMovie?.id)
            context.startActivity(intent)
        }
    }
}

class NetworkStateViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    fun bind(networkState: NetworkState?) {
        if(networkState === NetworkState.LOADING) {
            view.findViewById<TextView>(R.id.error_msg_item).visibility = View.GONE
            view.findViewById<ProgressBar>(R.id.progressbar_item).visibility = View.VISIBLE
        } else if(networkState === NetworkState.ERROR) {
            view.findViewById<TextView>(R.id.error_msg_item).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.error_msg_item).text = networkState.msg
            view.findViewById<ProgressBar>(R.id.progressbar_item).visibility = View.GONE
        } else if(networkState === NetworkState.ENDOFLIST) {
            view.findViewById<TextView>(R.id.error_msg_item).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.error_msg_item).text = networkState.msg
            view.findViewById<ProgressBar>(R.id.progressbar_item).visibility = View.GONE
        } else {
            view.findViewById<TextView>(R.id.error_msg_item).visibility = View.GONE
            view.findViewById<ProgressBar>(R.id.progressbar_item).visibility = View.GONE
        }
    }
}

class PopularMovieAdapter(private val context: Context): PagedListAdapter<PopularMovie, RecyclerView.ViewHolder>(
    DIFF_CONFIG
) {
    private var networkState: NetworkState? = null

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    companion object {
        val DIFF_CONFIG = object: DiffUtil.ItemCallback<PopularMovie>() {
            override fun areItemsTheSame(oldItem: PopularMovie, newItem: PopularMovie): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: PopularMovie, newItem: PopularMovie): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var view: View
        return if(viewType === MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            PopularMovieViewHolder(view,context)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item,parent,false)
            NetworkStateViewHolder(view)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) === MOVIE_VIEW_TYPE) {
            (holder as PopularMovieViewHolder).bind(getItem(position))
        } else {
            (holder as NetworkStateViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState !== NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if(hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if(hadExtraRow != hasExtraRow) {
            if(hadExtraRow) { //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount()) //remove the progressbar at the end
            } else { // hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount()) // add the progressbar at the end
            }
        } else if(hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true
            notifyItemChanged(itemCount - 1)
        }
    }
}
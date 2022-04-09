package com.example.tvshowapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tvshowapp.databinding.FragmentFavoritesBinding
import com.example.tvshowapp.databinding.FragmentMenuBinding
import com.google.firebase.auth.FirebaseAuth
import model.favoriteModel
import model.requestData
import model.tv_shows_model
import service.DataViewModel
import service.FavoriteViewModel
import service.TaskAdapter
import service.tasksViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private  lateinit var firebaseAuth: FirebaseAuth;
    private  var _binding: FragmentFavoritesBinding? =null;
    private val binding get() = _binding!!;
    private lateinit var viewModel: tasksViewModel;
    private lateinit var viewModelShows: DataViewModel;
    private lateinit var viewModelFavorites: FavoriteViewModel;
    private lateinit var lista:List<tv_shows_model>;
    private lateinit var view1:View;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance();
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root
        view1 =view;
        binding.taskRecycler2.adapter = TaskAdapter(emptyList());

        binding.favoriteBtn2.setOnClickListener {
            Navigation.findNavController(view1).navigate(R.id.action_favoritesFragment_to_menuFragment);

        }

        viewModel = ViewModelProvider(requireActivity())[tasksViewModel::class.java];
        viewModelShows= ViewModelProvider(requireActivity())[DataViewModel::class.java];
        viewModelFavorites= ViewModelProvider(requireActivity())[FavoriteViewModel::class.java];


        viewModelFavorites.favoriteList.observe(viewLifecycleOwner, Observer {
            var iterador = viewModelShows.dataList.value;
            if(iterador !==null){

                updateUi(iterador,it);


            }else{
                Toast.makeText(view1.context,"No hay peliculas para mostrar", Toast.LENGTH_SHORT).show();
            }
        })

        viewModelFavorites.loadData(firebaseAuth.currentUser!!.uid,view1.context);

        binding.logOutBtnAction2.setOnClickListener {
            logOut(view);
        }

        return view;
    }
    override fun onStart() {
        super.onStart()
        if(firebaseAuth.currentUser == null){
            Navigation.findNavController(view1).navigate(R.id.action_menuFragment_to_loginFragment);
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavoritesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoritesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun logOut(view: View){
        firebaseAuth.signOut();
        Navigation.findNavController(view).navigate(R.id.action_favoritesFragment_to_loginFragment);
    }

    private fun updateUi(listado: requestData, listadoFavoritos:List<favoriteModel>){

        val listaOrigen=listado.tv_shows.take(20);
        var lista = listaOrigen.filterIndexed{index, tvShowsModel ->listadoFavoritos.any { it.tvShowId==index.toString() }  }
       // lista = lista.filterIndexed{index, tvShowsModel -> listadoFavoritos.any { it.TvShowId==index.toString() } }


        val adapter = TaskAdapter(lista);
        //Toast.makeText(view1.context,listadoFavoritos[0].userId,Toast.LENGTH_LONG).show();

        adapter.setOnItemClickListener(object :TaskAdapter.onItemClickListener{
            override fun itemClick(id: Number) {


                viewModelFavorites.remove(firebaseAuth.currentUser!!.uid,id.toInt(),view1.context);
                Toast.makeText(view1.context,lista[id.toInt()].name+" se removio a favoritos",Toast.LENGTH_LONG).show();


            }

        });
        Toast.makeText(view1.context,"atachando",Toast.LENGTH_LONG).show();

        binding.taskRecycler2.adapter = adapter;
        binding.taskRecycler2.layoutManager = LinearLayoutManager(view1.context);


    }
}
package com.example.tvshowapp

import Data.task_table
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tvshowapp.databinding.FragmentMenuBinding
import com.google.firebase.auth.FirebaseAuth
import model.UserData
import model.requestData
import model.tv_shows_model
import service.DataViewModel
import service.TaskAdapter
import service.tasksViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private  lateinit var firebaseAuth: FirebaseAuth;
    private  var _binding:FragmentMenuBinding? =null;
    private val binding get() = _binding!!;
    private lateinit var viewModel: tasksViewModel;
    private lateinit var viewModelShows: DataViewModel;
    private lateinit var lista:List<tv_shows_model>;
    //private lateinit var listaFiltrada:List<task_table>;
    //private lateinit var firebaseDatabase: FirebaseDatabase;
    private lateinit var view1:View;
    private lateinit var userData: UserData;

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
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root
        view1 =view;
    binding.taskRecycler.adapter = TaskAdapter(emptyList());
    binding.profileBtn.setOnClickListener {
        Navigation.findNavController(view1).navigate(R.id.action_menuFragment_to_profileFragment);

    }
        binding.favoriteBtn.setOnClickListener {
            Navigation.findNavController(view1).navigate(R.id.action_menuFragment_to_favoritesFragment);

        }

        viewModel = ViewModelProvider(requireActivity())[tasksViewModel::class.java];
        viewModelShows=ViewModelProvider(requireActivity())[DataViewModel::class.java];
      /*  viewModelShows.dataList(firebaseAuth.currentUser?.uid!!).observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty()){
                //updateUi(it);
            }else{
                Toast.makeText(view1.context,"No hay tareas para mostrar", Toast.LENGTH_SHORT).show();
            }
        });*/
        viewModelShows.dataList.observe(viewLifecycleOwner, Observer {
            if(it !==null){
                updateUi(it);
                Toast.makeText(view1.context,it.total, Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(view1.context,"No hay peliculas para mostrar", Toast.LENGTH_SHORT).show();
            }
        })
        viewModelShows.loadData();
        viewModel.getProfileData(firebaseAuth.currentUser?.uid!!).observe(viewLifecycleOwner,
            Observer {
                if(it !=null){
                    viewModel.selectedProfile =it;
                }
            })
        binding.logOutBtnAction.setOnClickListener {
            logOut(view);
        }
        /*
         viewModel.dataList.observe(viewLifecycleOwner,{
            listado=it;
            Toast.makeText(view1.context,listado[0].title,Toast.LENGTH_LONG).show();
        })
        viewModel.error.observe(viewLifecycleOwner,{
            Toast.makeText(view1.context,it,Toast.LENGTH_LONG).show();
        })
        viewModel.loadData();

        */


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
         * @return A new instance of fragment MenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun logOut(view: View){
        firebaseAuth.signOut();
        Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_loginFragment);
    }

    private fun updateUi(listado:requestData){

            lista=listado.tv_show.take(20);
            val adapter = TaskAdapter(lista);
            adapter.setOnItemClickListener(object :TaskAdapter.onItemClickListener{
                override fun itemClick(id: Number) {

                   // viewModel.taskId = id;
                   // Navigation.findNavController(view1).navigate(R.id.action_menuFragment_to_taskDetailFragment);
                    Toast.makeText(view1.context,id.toString(),Toast.LENGTH_LONG).show();
                }

            });
        Toast.makeText(view1.context,"atachando",Toast.LENGTH_LONG).show();

        binding.taskRecycler.adapter = adapter;
            binding.taskRecycler.layoutManager = LinearLayoutManager(view1.context);


    }

   /* fun setSpinner(){

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            view1.context,
            R.array.spinnerList,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spinnerBar.adapter = adapter
        }
    }*/
/*
    fun filtrar(){

        if(binding.spinnerBar.selectedItem.toString()=="ninguno"){
            listaFiltrada=lista;
        }else{
            listaFiltrada=lista.filter { it.state==binding.spinnerBar.selectedItem.toString() }
        }

        val adapter = TaskAdapter(listaFiltrada);
        adapter.setOnItemClickListener(object :TaskAdapter.onItemClickListener{
            override fun itemClick(id: Number) {

                viewModel.taskId = id;
                Navigation.findNavController(view1).navigate(R.id.action_menuFragment_to_taskDetailFragment);
                //Toast.makeText(view1.context,id.toString(),Toast.LENGTH_LONG).show();
            }

        });
        binding.taskRecycler.adapter = adapter;
        binding.taskRecycler.layoutManager = LinearLayoutManager(view1.context);
    }*/
}
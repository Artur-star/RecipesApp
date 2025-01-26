package com.knyazev.recipesapp.ui.recipes.recipe

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.knyazev.recipesapp.Constants.MIN_PORTIONS
import com.knyazev.recipesapp.R
import com.knyazev.recipesapp.databinding.FragmentRecipeBinding
import com.knyazev.recipesapp.ui.recipes.adaptersRecipes.IngredientsAdapter
import com.knyazev.recipesapp.ui.recipes.adaptersRecipes.MethodAdapter

class RecipeFragment : Fragment() {
    private val recipeFragmentArgs: RecipeFragmentArgs by navArgs()
    private var ingredientAdapter = IngredientsAdapter(emptyList())
    private var methodAdapter = MethodAdapter(emptyList())
    private val viewModel: RecipeViewModel by viewModels()
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalArgumentException("Binding for FragmentRecipeBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recipeId = recipeFragmentArgs.recipeId
        viewModel.loadRecipe(recipeId = recipeId)
        initUI()
    }

    private fun initUI() {
        viewModel.recipeStateLD.observe(viewLifecycleOwner) { (flag, countPortion, recipe, recipeImage) ->
            if (recipe == null) {
                Toast.makeText(
                    Application().applicationContext,
                    "Ошибка получения данных",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (flag) {
                binding.ibHeaderHeart.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_big_red_heart
                    )
                )
            } else {
                binding.ibHeaderHeart.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_big_heart
                    )
                )
            }
            binding.ivHeaderRecipe.setImageDrawable(recipeImage)
            binding.tvHeaderRecipe.text = recipe?.title

            binding.countPortions.text = countPortion.toString()
            ingredientAdapter.updateIngredients(recipe?.ingredients ?: emptyList(), countPortion)
            methodAdapter.updateMethod(recipe?.method ?: emptyList())

            binding.ibHeaderHeart.setOnClickListener {
                viewModel.onFavoritesClicked(
                    recipe?.id ?: 0
                )
            }
        }
        binding.countPortions.text = MIN_PORTIONS

        binding.rvIngredients.adapter = ingredientAdapter
        binding.rvMethod.adapter = methodAdapter

        val portionSeekBar = PortionSeekBarListener(onChangeIngredients = { portion ->
            viewModel.setCountPortions(portion)
        })
        binding.sbPortions.setOnSeekBarChangeListener(portionSeekBar)

        val divider =
            MaterialDividerItemDecoration(requireContext(), MaterialDividerItemDecoration.VERTICAL)
        divider.isLastItemDecorated = false
        divider.setDividerColorResource(requireContext(), R.color.divider)
        divider.setDividerInsetStartResource(requireContext(), R.dimen.main_space_8)
        divider.setDividerInsetEndResource(requireContext(), R.dimen.main_space_8)

        binding.rvMethod.addItemDecoration(divider)
        binding.rvIngredients.addItemDecoration(divider)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) : OnSeekBarChangeListener {
    override fun onProgressChanged(p0: SeekBar?, position: Int, p2: Boolean) {
        onChangeIngredients(position)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }
}
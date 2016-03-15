package com.dougritter.marvelmovies

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dougritter.marvelmovies.databinding.ItemCharacterBinding

class CharactersAdapter(var characterResponse: Model.CharacterResponse) : RecyclerView.Adapter<CharactersAdapter.ItemCharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCharacterViewHolder {
        val itemCharacterBinding = DataBindingUtil.inflate<ItemCharacterBinding>(LayoutInflater.from(parent.context), R.layout.item_character, parent, false)

        return ItemCharacterViewHolder(itemCharacterBinding)

    }

    override fun onBindViewHolder(holder: ItemCharacterViewHolder, position: Int) {
        holder.bindItemCharacter(characterResponse.data.results[position])
    }

    override fun getItemCount() = characterResponse.data.results.size

    class ItemCharacterViewHolder(val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.cardView) {
        fun bindItemCharacter(character: Model.Character) {
            binding.viewmodel = ViewModel.CharacterViewModel(itemView.context, character)

            // COMMENTED BECAUSE THE VIEW WAS RECYCLING AND THE IMAGE WAS NOT LOADING AGAIN
            // NEED TO FIX LATER
            /*if (binding.viewmodel == null) {
                binding.viewmodel = ViewModel.CharacterViewModel(itemView.context, character)
            } else {
                var charc: ViewModel.CharacterViewModel = binding.viewmodel
                charc.model = character
                binding.viewmodel = ViewModel.CharacterViewModel(itemView.context, character)
            }*/

        }

    }

}


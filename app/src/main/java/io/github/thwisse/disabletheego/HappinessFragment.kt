package io.github.thwisse.disabletheego

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.ImageLoader
import coil.decode.GifDecoder
import io.github.thwisse.disabletheego.databinding.FragmentHappinessBinding
import coil.load
import coil.request.ImageRequest

class HappinessFragment : Fragment() {

    private var _binding: FragmentHappinessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHappinessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ImageLoader ile GIF'i yükleyip oynatma
        val imageLoader = ImageLoader.Builder(requireContext())
            .components {
                add(GifDecoder.Factory())
            }
            .build()

        val request = ImageRequest.Builder(requireContext())
            .data(R.drawable.img_happiness) // your_gif_file yerine kendi GIF dosyanızın adını yazın
            .target(binding.imgHappiness) // gifImageView, XML'deki ImageView'in ID'si
            .build()

        imageLoader.enqueue(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
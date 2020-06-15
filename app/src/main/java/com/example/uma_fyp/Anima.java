package com.example.uma_fyp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class Anima extends Fragment {


    Animation topAnima, bottomAnima;
    CircleImageView imageLogo;
    ImageView name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.startingpage, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        topAnima = AnimationUtils.loadAnimation(getContext(),R.anim.top_animation);
        bottomAnima = AnimationUtils.loadAnimation(getContext(),R.anim.bottom_animation);

        imageLogo = getActivity().findViewById(R.id.circular_image);
        name = getActivity().findViewById(R.id.nameimage);

        imageLogo.setAnimation(topAnima);
        name.setAnimation(bottomAnima);


    }
}

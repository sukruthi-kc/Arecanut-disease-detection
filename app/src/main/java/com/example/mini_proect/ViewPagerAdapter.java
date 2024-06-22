package com.example.mini_proect;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new SignupTabFragment();
        }
        return new LoginTabFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public static class LoginTabFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_login_tab, container, false);

            // Find the "USER LOGIN" button and set OnClickListener
            Button userLoginButton = view.findViewById(R.id.user_login_button);
            userLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MainActivity.class); // Change MainActivity to Login
                    startActivity(intent);
                }
            });

            return view;
        }
    }

    public static class SignupTabFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_signup_tab, container, false);

            // Find the "ADMIN LOGIN" button and set OnClickListener
            Button adminLoginButton = view.findViewById(R.id.admin_login_button);
            adminLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AdminLogin.class);
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}

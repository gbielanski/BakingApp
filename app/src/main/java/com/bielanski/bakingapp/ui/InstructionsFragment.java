package com.bielanski.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bielanski.bakingapp.R;

public class InstructionsFragment extends Fragment {
    public InstructionsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instructions, container, false);
        TextView instructionsTextView = rootView.findViewById(R.id.instructions_text_view);
        instructionsTextView.setText("This is some test instructions. This will change in the runtime");
        instructionsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StepDetailsActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}

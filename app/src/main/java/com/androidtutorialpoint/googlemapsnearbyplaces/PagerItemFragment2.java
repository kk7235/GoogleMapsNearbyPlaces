package com.androidtutorialpoint.googlemapsnearbyplaces;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle i
 */
public class PagerItemFragment2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
 /*   @BindView(R.id.imageView)
    ImageView image;
    @BindView(R.id.name)


    @BindView(R.id.viccinity)
    TextView viccinity;
    @BindView(R.id.rating)
    TextView rating;*/
    int pos;
 static  String kr;  TextView name;
    SharedPreferences sharedPreferences;
int position1;
   // private Unbinder unbinder;

    public PagerItemFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RedemdemptionViewPagerItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PagerItemFragment2 newInstance(int position, String ar) {
       PagerItemFragment2 fragment = new PagerItemFragment2();
        Bundle args = new Bundle();
       kr =ar;

        args.putInt(ARG_PARAM1, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments() != null ? getArguments().getInt(ARG_PARAM1) : 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment_home_new for this fragment
        View view = inflater.inflate(R.layout.neww, container, false);

       // name.setText(kr);
        //viccinity.setText(kr.get(pos).getViccnity());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
name=view.findViewById(R.id.name);
        name.setText(kr);
    }
}
package com.jonathan.geoffroy.vlille_analyser.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.jonathan.geoffroy.vlille_analyser.R;
import com.jonathan.geoffroy.vlille_analyser.model.StationOrderBy;

import static android.view.View.OnClickListener;


/**
 * Fragment permettant de choisir les options d'affichage de la liste des stations:
 *  * ORDER BY
 *  * juste les favoris ?
 */
public class StationsOptionsFragment extends Fragment {

    private OnOptionsFragmentInteractionListener mListener;

    public StationsOptionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StationsOptionsFragment.
     */
    public static StationsOptionsFragment newInstance() {
        StationsOptionsFragment fragment = new StationsOptionsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_stations_options, container, false);

        final CheckBox automaticRefresh = (CheckBox) inflatedView.findViewById(R.id.automaticRefresh_cb);
        automaticRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAutomaticRefreshChanged(automaticRefresh.isChecked());
            }
        });

        RadioGroup orderBy = (RadioGroup) inflatedView.findViewById(R.id.orderBy_group);
        orderBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                mListener.onOrderByChanged(StationOrderBy.valueOf(id));
            }
        });

        CheckBox onlyStar = (CheckBox) inflatedView.findViewById(R.id.star_toggle);
        onlyStar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mListener.onOnlyStarChanged(b);
            }
        });

        return inflatedView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnOptionsFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnOptionsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnOptionsFragmentInteractionListener {
        /**
         * appelé lorsque le rafraichissement automatique change de position
         *
         * @param isChecked si le rafraichissement automatique est activé
         */
        void onAutomaticRefreshChanged(boolean isChecked);

        /**
         * Appelé lorsque la façon d'ordonner doit être changé
         * @param orderBy la nouvelle façon d'ordonner la liste des stations
         */
        void onOrderByChanged(StationOrderBy orderBy);

        /**
         * Appelé lorsque "n'afficher que les favoris" change de positions
         * @param isChecked si il faut n'afficher que les favoris
         */
        void onOnlyStarChanged(boolean isChecked);
    }

}

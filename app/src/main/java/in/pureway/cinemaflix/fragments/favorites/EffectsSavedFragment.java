package in.pureway.cinemaflix.fragments.favorites;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.AdapterSavedEffects;

/**
 * A simple {@link Fragment} subclass.
 */
public class EffectsSavedFragment extends Fragment {
private View view;
private RecyclerView rv_effects_saved;
    public EffectsSavedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_effects_saved, container, false);

        findIds();
        return view;
    }

    private void findIds() {
        rv_effects_saved=view.findViewById(R.id.rv_effects_saved);

        rv_effects_saved.setAdapter(new AdapterSavedEffects(getActivity(), new AdapterSavedEffects.SelectEffect() {
            @Override
            public void chooseEffect(int position) {

            }
        }));
    }
}

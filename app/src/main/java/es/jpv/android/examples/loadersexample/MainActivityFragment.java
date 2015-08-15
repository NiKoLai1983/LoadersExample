package es.jpv.android.examples.loadersexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import es.jpv.android.examples.loadersexample.dummy.DummyContent;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
        implements RVCursorAdapter.OnItemClickListener, RVCursorAdapter.OnItemLongClickListener {

    RVCursorAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        adapter = new RVCursorAdapter(getActivity(), DummyContent.ITEMS_CURSOR);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onItemClick(View v, int position) {
        Toast.makeText(getActivity(),
                "Clicked " + ((TextView) v.findViewById(R.id.textView)).getText() +
                        " on position " + position,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View v, int position) {
        Toast.makeText(getActivity(),
                "Long-clicked " + ((TextView) v.findViewById(R.id.textView)).getText() +
                        " on position " + position,
                Toast.LENGTH_SHORT).show();
    }
}

package com.example.pub;
import com.example.pub.R;


import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PackingListAdapter extends RecyclerView.Adapter<PackingListAdapter.ViewHolder> {

    private List<String> packingItems;
    private Set<Integer> selectedItems;

    public PackingListAdapter(List<String> items) {
        this.packingItems = items;
        this.selectedItems = new HashSet<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_packing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = packingItems.get(position);
        holder.checkBox.setText(item);
        holder.checkBox.setChecked(selectedItems.contains(position));
    }

    @Override
    public int getItemCount() {
        return packingItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (isChecked) {
                            selectedItems.add(position);
                        } else {
                            selectedItems.remove(position);
                        }
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (!checkBox.isChecked()) { // Check if the item is not checked
                            packingItems.remove(position);
                            notifyItemRemoved(position);
                        } else {
                            Toast toast = Toast.makeText(v.getContext(), "Cannot delete selected items", Toast.LENGTH_SHORT);
                            View toastView = toast.getView();
                            toastView.setBackgroundColor(v.getContext().getResources().getColor(R.color.toast_background_color));

                            // Make the toast message bold
                            TextView textView = toastView.findViewById(android.R.id.message);
                            if (textView != null) {
                                SpannableString spannableString = new SpannableString(textView.getText());
                                spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                textView.setText(spannableString);
                            }

                            toast.show();
                        }
                    }
                }
            });



        }
    }

    public List<String> getSelectedItems() {
        List<String> selected = new ArrayList<>();
        for (Integer position : selectedItems) {
            selected.add(packingItems.get(position));
        }
        return selected;
    }

    public void resetCheckedState() {
        selectedItems.clear();
        notifyDataSetChanged();
    }
}
package com.hutech.exampractice.Adapter;

import static com.hutech.exampractice.DbQuery.ANSWERED;
import static com.hutech.exampractice.DbQuery.REVIEW;
import static com.hutech.exampractice.DbQuery.UNANSWERED;
import static com.hutech.exampractice.DbQuery.g_quesList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hutech.exampractice.DbQuery;
import com.hutech.exampractice.R;

import java.util.List;

import com.hutech.exampractice.Models.QuestionModel;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private List<QuestionModel> questionsList;
    public QuestionsAdapter(List<QuestionModel>questionsList){
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(i);
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ques;
        private Button optionA, optionB, optionC, optionD, prevSelectedB;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ques = itemView.findViewById(R.id.tv_question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            prevSelectedB = null;

        }
        private void setData(final int pos){
            ques.setText(questionsList.get(pos).getQuestion());
            optionA.setText(questionsList.get(pos).getOptionA());
            optionB.setText(questionsList.get(pos).getOptionB());
            optionC.setText(questionsList.get(pos).getOptionC());
            optionD.setText(questionsList.get(pos).getOptionD());

            setOption(optionA, 1, pos);
            setOption(optionB, 2, pos);
            setOption(optionC, 3, pos);
            setOption(optionD, 4, pos);


            // click vào đáp án A
            optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionA, 1, pos);
                }
            });

            // click vào đáp án B
            optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionB, 2, pos);
                }
            });

            // click vào đáp án C
            optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionC, 3, pos);
                }
            });

            // click vào đáp án D
            optionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionD, 4, pos);
                }
            });
        }

        // Chọn vào 1 đáp án
        private void selectOption(Button btn, int option_num, int quesID){
            if(prevSelectedB == null){
                btn.setBackgroundResource(R.drawable.selected_btn);
                DbQuery.g_quesList.get(quesID).setSelectedAns(option_num);

                changeStatus(quesID, ANSWERED);

                prevSelectedB = btn;
            }else{

                if(prevSelectedB.getId() == btn.getId()){
                    btn.setBackgroundResource(R.drawable.unselected_btn);
                    DbQuery.g_quesList.get(quesID).setSelectedAns(-1);

                    changeStatus(quesID, UNANSWERED);


                    prevSelectedB = null;
                }
                else{
                    prevSelectedB.setBackgroundResource(R.drawable.unselected_btn);
                    btn.setBackgroundResource(R.drawable.selected_btn);

                    DbQuery.g_quesList.get(quesID).setSelectedAns(option_num);

                    changeStatus(quesID, ANSWERED);
                    prevSelectedB = btn;
                }
            }
        }
    }

    // Thay đổi trạng thái
    private void changeStatus(int id, int status){
        if(g_quesList.get(id).getStatus() != REVIEW)
        {
            g_quesList.get(id).setStatus(status);
        }
    }

    private void setOption(Button btn, int option_num, int quesID){
        if(DbQuery.g_quesList.get(quesID).getSelectedAns() == option_num){
            btn.setBackgroundResource(R.drawable.selected_btn);
        }else{
            btn.setBackgroundResource(R.drawable.unselected_btn);
        }
    }
}

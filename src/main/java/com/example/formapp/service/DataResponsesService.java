package com.example.formapp.service;

import java.util.List;

import com.example.formapp.model.appform.Answers;
import com.example.formapp.model.appform.Responses;

public interface DataResponsesService {

	public void saveAndUpdate(Responses responses, List<Answers> answers);

}

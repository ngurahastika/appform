package com.example.formapp.service;

import java.util.List;

import com.example.formapp.model.appform.AllowedDomain;
import com.example.formapp.model.appform.Form;

public interface DataFormService {

	public void saveAndUpdate(Form form, List<AllowedDomain> allowedDomain);

}

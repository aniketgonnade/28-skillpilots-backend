package com.skilladmin.service;

import java.util.List;

import com.skilladmin.model.AppUpdate;

public interface AppUpdateService {

	 public List<AppUpdate> findByVersionId();
}

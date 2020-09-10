///*
// * JBoss, Home of Professional Open Source
// * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
// * contributors by the @authors tag. See the copyright.txt in the
// * distribution for a full listing of individual contributors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
//*/
//package org.escola.auth;
//
//import java.io.Serializable;
//
//import javax.faces.view.ViewScoped;
//import javax.inject.Named;
//import javax.servlet.annotation.WebListener;
//import javax.servlet.http.HttpSessionBindingEvent;
//import javax.servlet.http.HttpSessionBindingListener;
//import javax.servlet.http.HttpSessionEvent;
//import javax.servlet.http.HttpSessionListener;
//
//@WebListener
//public class LoggedUserController implements HttpSessionBindingListener {
//
//	@Override
//	public void valueBound(HttpSessionBindingEvent event) {
//		System.out.println("-- HttpSessionBindingListener#valueBound() --");
//		System.out.printf("added attribute name: %s, value:%s %n", event.getName(), event.getValue());
//	}
//
//	@Override
//	public void valueUnbound(HttpSessionBindingEvent event) {
//		System.out.println("-- HttpSessionBindingEvent#valueUnbound() --");
//		System.out.printf("removed attribute name: %s, value:%s %n", event.getName(), event.getValue());
//	}
//}

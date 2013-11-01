package servertester.controllers;

import client.Communicator;
import java.net.MalformedURLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import servertester.views.*;
import shared.communication.*;

public class Controller implements IController {

        private static final String PROTOCOL = "http";
    
	private IView _view;
        private Communicator communicator;
	
	public Controller() {
		return;
	}
	
	public IView getView() {
		return _view;
	}
	
	public void setView(IView value) {
		_view = value;
	}
	
	// IController methods
	//
	
	@Override
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("39640");
		operationSelected();
            try {
                communicator = new Communicator(PROTOCOL, getView().getHost(), Integer.parseInt(getView().getPort()));
            } catch (Communicator.CommunicatorException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	@Override
	public void operationSelected() {
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add("User");
		paramNames.add("Password");
		
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			break;
		case GET_PROJECTS:
			break;
		case GET_SAMPLE_IMAGE:
			paramNames.add("Project");
			break;
		case DOWNLOAD_BATCH:
			paramNames.add("Project");
			break;
		case GET_FIELDS:
			paramNames.add("Project");
			break;
		case SUBMIT_BATCH:
			paramNames.add("Batch");
			paramNames.add("Record Values");
			break;
		case SEARCH:
			paramNames.add("Fields");
			paramNames.add("Search Values");
			break;
		default:
			assert false;
			break;
		}
		
		getView().setRequest("");
		getView().setResponse("");
		getView().setParameterNames(paramNames.toArray(new String[paramNames.size()]));
	}

	@Override
	public void executeOperation() {
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			validateUser();
			break;
		case GET_PROJECTS:
			getProjects();
			break;
		case GET_SAMPLE_IMAGE:
			getSampleImage();
			break;
		case DOWNLOAD_BATCH:
			downloadBatch();
			break;
		case GET_FIELDS:
			getFields();
			break;
		case SUBMIT_BATCH:
			submitBatch();
			break;
		case SEARCH:
			search();
			break;
		default:
			assert false;
			break;
		}
	}
        
        private void setCommunicatorHttp() {
            communicator.setProtocol(PROTOCOL);
            communicator.setHost(getView().getHost());
            communicator.setPort(Integer.parseInt(getView().getPort()));
        }
	
	private void validateUser() {
            setCommunicatorHttp();
            String[] parameterValues = getView().getParameterValues();
            assert parameterValues.length == 2;
            if (hasEmpty(parameterValues)) {
                getView().setResponse("FAILED\n");
                return;
            }
            
            String username = parameterValues[0];
            String password = parameterValues[1];
            
            ValidateUser_Param params = new ValidateUser_Param(username, password);
            ValidateUser_Result result = communicator.validateUser(params);
            String response;
            if (result == null) {
                response = "FAILED\n";
            }
            else {
                response = result.toString();
            }
            getView().setResponse(response);
	}
	
	private void getProjects() {
            setCommunicatorHttp();
            String[] parameterValues = getView().getParameterValues();
            assert parameterValues.length == 2;
            if (hasEmpty(parameterValues)) {
                getView().setResponse("FAILED\n");
                return;
            }
            
            String username = parameterValues[0];
            String password = parameterValues[1];
            
            GetProjects_Param params = new GetProjects_Param(username, password);
            GetProjects_Result result = communicator.getProjects(params);
            String response;
            if (result == null) {
                response = "FAILED\n";
            }
            else {
                response = result.toString();
            }
            getView().setResponse(response);
	}
	
	private void getSampleImage() {
            setCommunicatorHttp();
            String[] parameterValues = getView().getParameterValues();
            assert parameterValues.length == 3;
            if (hasEmpty(parameterValues)) {
                getView().setResponse("FAILED\n");
                return;
            }
            
            String username = parameterValues[0];
            String password = parameterValues[1];
            int projectId = Integer.parseInt(parameterValues[2]);
            
            GetSampleImage_Param params = new GetSampleImage_Param(username, password, projectId);
            GetSampleImage_Result result = communicator.getSampleImage(params);
            String response;
            if (result == null) {
                response = "FAILED\n";
            }
            else {
                try {
                    response = result.toString(PROTOCOL, getView().getHost(), Integer.parseInt(getView().getPort()));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    response = "FAILED\n";
                }
            }
            getView().setResponse(response);
	}
	
	private void downloadBatch() {
            setCommunicatorHttp();
            String[] parameterValues = getView().getParameterValues();
            assert parameterValues.length == 3;
            if (hasEmpty(parameterValues)) {
                getView().setResponse("FAILED\n");
                return;
            }
            
            String username = parameterValues[0];
            String password = parameterValues[1];
            int projectId = Integer.parseInt(parameterValues[2]);
            
            DownloadBatch_Param params = new DownloadBatch_Param(username, password, projectId);
            DownloadBatch_Result result = communicator.downloadBatch(params);
            String response;
            if (result == null) {
                response = "FAILED\n";
            }
            else {
                try {
                    response = result.toString(PROTOCOL, getView().getHost(), Integer.parseInt(getView().getPort()));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    response = "FAILED\n";
                }
            }
            getView().setResponse(response);
	}
	
	private void getFields() {
            setCommunicatorHttp();
            String[] parameterValues = getView().getParameterValues();
            assert parameterValues.length == 3;
            if (hasEmpty(parameterValues)) {
                getView().setResponse("FAILED\n");
                return;
            }
            
            String username = parameterValues[0];
            String password = parameterValues[1];
            int projectId = Integer.parseInt(parameterValues[2]);
            
            GetFields_Param params = new GetFields_Param(username, password, projectId);
            GetFields_Result result = communicator.getFields(params);
            String response;
            if (result == null) {
                response = "FAILED\n";
            }
            else {
                response = result.toString();
            }
            getView().setResponse(response);
	}
	
	private void submitBatch() {
            setCommunicatorHttp();
            String[] parameterValues = getView().getParameterValues();
            assert parameterValues.length == 4;
            if (hasEmpty(parameterValues)) {
                getView().setResponse("FAILED\n");
                return;
            }
            
            String username = parameterValues[0];
            String password = parameterValues[1];
            int batchId = Integer.parseInt(parameterValues[2]);
            String values = parameterValues[3];
            
            SubmitBatch_Param params = new SubmitBatch_Param(username, password, batchId, values);
            SubmitBatch_Result result = communicator.submitBatch(params);
            String response;
            if (result == null) {
                response = "FAILED\n";
            }
            else {
                response = result.toString();
            }
            getView().setResponse(response);
	}
	
	private void search() {
            setCommunicatorHttp();
            String[] parameterValues = getView().getParameterValues();
            assert parameterValues.length == 4;
            if (hasEmpty(parameterValues)) {
                getView().setResponse("FAILED\n");
                return;
            }
            
            String username = parameterValues[0];
            String password = parameterValues[1];
            String fields = parameterValues[2];
            String values = parameterValues[3];
            
            Search_Param params = new Search_Param(username, password, fields, values);
            Search_Result result = communicator.search(params);
            String response;
            if (result == null) {
                response = "FAILED\n";
            }
            else {
                try {
                    response = result.toString(PROTOCOL, getView().getHost(), Integer.parseInt(getView().getPort()));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    response = "FAILED\n";
                }
            }
            getView().setResponse(response);
	}
        
        private boolean hasEmpty(String[] values) {
            for (String s : values) {
                if (s.isEmpty()) {
                    return true;
                }
            }
            return false;
        }

}


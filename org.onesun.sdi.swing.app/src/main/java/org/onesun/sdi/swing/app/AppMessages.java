/*
   Copyright 2011 Udayakumar Dhansingh (Udy)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
 */
package org.onesun.sdi.swing.app;

public interface AppMessages {
	// Error Messages
	public final String ERROR_INVALID_CONNECTION						= "Invalid Connection ... You must at least provide connection name!";
	public final String ERROR_CONNECTION_NOT_SELECTED					= "A Connection  must be selected ... Cannot delete nothing!";
	public final String ERROR_PROVIDER_NOT_SELECTED						= "A Third-Party provider must be chosen to complete this activity!";
	public final String ERROR_INVALID_OAUTH_DETAILS						= "Connection name, third-party provider API key and secret is must to complete this activity!";
	public final String ERROR_DOCUMENTS_MISSING							= "A Schema and data document must be chosen to complete this activity!";
	public final String ERROR_SCHEMA_DOCUMENT_MISSING					= "A discovered schema or an existing schema document is required to complete this activity!";
	public final String ERROR_UNKNOWN_TEXT_FORMAT						= "Text format cannot be determined, hence schema cannot be created (Known formats: XML, ATOM, RSS, JSON)!";
	public final String ERROR_RESPONSE_PAYLOAD_MISSING					= "A valid response (or a data file) is required to complete this activity!";
	public final String ERROR_TOKEN_MISSING								= "Access Token (OAUTH) is required to complete this activity!";
	public final String ERROR_BAD_HTTP_PROTOCOL							= "Unknown HTTP protocol, (http/https) must be specified!";
	public final String ERROR_BAD_URL									= "A Valid URL is required to complete this activity!";
	public final String	ERROR_BAD_FILE_PATH 							= "An absolute path to file is required";
	public final String ERROR_NO_DATA 									= "Cannot process; Obtain data before proceeding further";
	public final String ERROR_NO_DATA_TO_ENTRICH 						= "Cannot invoke services; Obtain data before proceeding further";
	public final String ERROR_NO_DATA_TO_EXTRACT_METADATA 				= "Cannot extract metadata; Obtain data before proceeding further";
	public final String ERROR_NO_PREVIEW_NEEDS_DATA_AND_METADATA 		= "Cannot create a preview; Obtain data before proceeding further";
	public final String ERROR_MISSING_DATA_SERVICE_AND_COLUMNS 			= "At least one data service must be selected and list of columns must be provided";
	public final String ERROR_NO_METADATA 								= "Metadata is not available to create columns";
	public final String ERROR_SYSTEM_CLOCK_IS_NOT_CURRENT 				= "Your system clock is not set to right values, consider synchronizing system clock with a time server";
	
	// Informational Messages
	public final String INFORMATION_CHOOSE_A_CONNECTION					= "Choose a connection before proceeding further";
	public final String 
		INFORMATION_CHOOSE_A_CONNECTION_OR_UNSUPPORTED_FEATURE 			= "You've not selected a connection instance!<br>(or)<br>Your selection is currently unsupported!";
	
	// UI Messages
	
}

### Required
---
- **title**: String
- **flags**: Array
    - **name**: String
    - **type**: [bool, int, float, string, extVal]
    - **value**: String - [1/0 for bool]
- **flagPackage**: String
- **appPackage**: String

### Optional:
---
- **source**: String?
- **warning**: String? 
- **description**: String? 
- **detailsLink**: String?
- **isBeta**: Boolean? 
- **isEnabled**: Boolean = true 
- **minAppVersionCode**: Int?
- **minAndroidSdkCode**: Int?
- **group**: String?
- **isPrimary**: Boolean? 
- **tag**: String?

# Example
---
```json
{  
"title": "Flag's title",  
"source": "Name Surname",  
"warning": "Important warning",  
"description": "Flag's description",  
"isBeta": true,
"isPrimary": false,
"tag": "flag_tag",
"group": "GroupName",
"detailsLink": "https://t.me/gmsflags_content/0",  
"flagPackage": "com.package.name#com.subpackage.name",  
"appPackage": "com.package.name",  
"minAppVersionCode": 12345678,  
"minAndroidSdkCode": 30,  
"isEnabled": true,  
"flags": [  
	  {  
	    "name": "00000000",  
	    "type": "bool",  
	    "value": "1"  
	  } 
	]
}
```
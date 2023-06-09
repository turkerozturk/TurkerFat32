/***********************************************************************
 * Copyright 2023 Turker Ozturk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***********************************************************************/


package com.turkerozturk.library.fat32.exceptions;

/**
 *
 * @author u
 */
public class PathNotExistException extends Exception{
    
    //https://stackoverflow.com/questions/8423700/how-to-create-a-custom-exception-type-in-java
    
    public PathNotExistException () {

    }

    public PathNotExistException (String message) {
        super (message);
    }

    public PathNotExistException (Throwable cause) {
        super (cause);
    }

    public PathNotExistException (String message, Throwable cause) {
        super (message, cause);
    }
    
}

/*
 * Copyright (C) 2023 Christian Pöcksteiner (christian.poecksteiner@aon.at)
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *         https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cpo1964.platform.selenium;

/**
 * The enum Webelement type.
 *
 * 	definitions of standard webelement types
 * 	use this values for element definitions in page objects
 */
public enum WebelementType {

    /**
     * Editfield webelement type.
     */
    EDITFIELD,
    /**
     * Listbox webelement type.
     */
    LISTBOX,
    /**
     * Radiogroup webelement type.
     */
    RADIOGROUP,
    /**
     * Radiobutton webelement type.
     */
    RADIOBUTTON,
    /**
     * Checkbox webelement type.
     */
    CHECKBOX,
    /**
     * Numericfield webelement type.
     */
    NUMERICFIELD,
    /**
     * Filefield webelement type.
     */
    FILEFIELD,
    /**
     * Slider webelement type.
     */
    SLIDER,
    /**
     * Button webelement type.
     */
    BUTTON,
    /**
     * Link webelement type.
     */
    LINK,
    /**
     * Text webelement type.
     */
    TEXT;
}

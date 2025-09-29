package cz.mendelu.project.ui.screens.routines.addedit

import cz.mendelu.project.model.Item
import cz.mendelu.project.model.Routine

class RoutineAddEditScreenData {
    var item : Item = Item()
    var routine: Routine = Routine()
    var isActive : Boolean = true
    var showTimeInput : Boolean = false
    var frequencyManualInput : String = ""
    var timeInput : String = ""
    var dateInput : String = ""
    var selectedFrequencyOption : FrequencyPicker = FrequencyPicker.DAILY
    var startingActiveState : Boolean = true
    var errors: RoutineAddEditErrors = RoutineAddEditErrors()
}
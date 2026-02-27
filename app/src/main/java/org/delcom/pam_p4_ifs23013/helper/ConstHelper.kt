package org.delcom.pam_p4_ifs23013.helper

class ConstHelper {
    // Route Names
    enum class RouteNames(val path: String) {
        Home(path = "home"),
        Profile(path = "profile"),

        Plants(path = "plants"),
        PlantsAdd(path = "plants/add"),

        PlantsDetail(path = "plants/{plantId}"),
        PlantsEdit(path = "plants/{plantId}/edit"),
        Animals(path = "animals"),
        AnimalsAdd(path = "animals/add"),

        AnimalsDetail(path = "animals/{animalId}"),
        AnimalsEdit(path = "animals/{animalId}/edit"),

    }
}
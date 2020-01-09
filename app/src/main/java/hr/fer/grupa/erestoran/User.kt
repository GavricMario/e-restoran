package hr.fer.grupa.erestoran

class User(
    var username: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var creditCard: String = "",
    var password: String = "",
    var language: String= "hr",
//    var history:
    var settingItemUsage: ArrayList<SettingsItemUsageModel> = ArrayList(),
    var addresses: ArrayList<AddressModel> = ArrayList()
)
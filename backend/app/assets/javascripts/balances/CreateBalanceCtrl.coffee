class CreateBalanceCtrl

    constructor: (@$log, @$location,  @BalanceService) ->
        @$log.debug "constructing CreateBalanceController"
        @balance = {}

    createBalance: () ->
        @$log.debug "createBalance()"
        @BalanceService.createBalance(@balance)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} Balance"
                @user = data
                @$location.path("/")
            ,
            (error) =>
                @$log.error "Unable to create Balance: #{error}"
            )

controllersModule.controller('CreateBalanceCtrl', ['$log', '$location', 'BalanceService', CreateBalanceCtrl])
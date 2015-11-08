
class BalanceCtrl

    constructor: (@$log, @BalanceService) ->
        @$log.debug "constructing BalanceController"
        @balances = []
        @getAllBalances()

    getAllBalances: () ->
        @$log.debug "getAllBalances()"

        @BalanceService.listBalances()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Balances"
                @balances = data
            ,
            (error) =>
                @$log.error "Unable to get Balances: #{error}"
            )

controllersModule.controller('BalanceCtrl', ['$log', 'BalanceService', BalanceCtrl])
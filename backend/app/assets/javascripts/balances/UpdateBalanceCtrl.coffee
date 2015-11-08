class UpdateBalanceCtrl

  constructor: (@$log, @$location, @$routeParams, @BalanceService) ->
      @$log.debug "constructing UpdateBalanceController"
      @balance = {}
      @findBalance()

  updateBalance: () ->
      @$log.debug "updateBalance()"
      @BalanceService.updateBalance(@$routeParams.account, @balance)
      .then(
          (data) =>
            @$log.debug "Promise returned #{data} Balance"
            @balance = data
            @$location.path("/")
        ,
        (error) =>
            @$log.error "Unable to update Balance: #{error}"
      )

  deleteBalance: () ->
      @$log.debug "deleteBalance()"
      @BalanceService.deleteBalance(@$routeParams.account, @balance)
      .then(
          (data) =>
            @$log.debug "Promise returned #{data} Balance"
            @balance = data
            @$location.path("/")
        ,
        (error) =>
            @$log.error "Unable to delete Balance: #{error}"
      )

  findBalance: () ->
      # route params must be same name as provided in routing url in app.coffee
      account    = @$routeParams.account
      @$log.debug "findBalance route params: #{account}"

      @BalanceService.listBalances()
      .then(
        (data) =>
          @$log.debug "Promise returned #{data.length} Balances"

          @balance = (data.filter (balance) -> balance.account is account )[0]
      ,
        (error) =>
          @$log.error "Unable to get Balances: #{error}"
      )

controllersModule.controller('UpdateBalanceCtrl', ['$log', '$location', '$routeParams', 'BalanceService', UpdateBalanceCtrl])
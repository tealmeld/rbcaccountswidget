
class BalanceService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing BalanceService"

    listBalances: () ->
        @$log.debug "listBalances()"
        deferred = @$q.defer()

        @$http.get("/balances")
        .success((data, status, headers) =>
                @$log.info("Successfully listed Balances - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list Balances - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    createBalance: (balance) ->
        @$log.debug "createBalance #{angular.toJson(balance, true)}"
        deferred = @$q.defer()

        @$http.post('/balance', balance)
        .success((data, status, headers) =>
                @$log.info("Successfully created Balance - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to create balance - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    deleteBalance: (account, balance) ->
        @$log.debug "deleteBalance #{angular.toJson(balance, true)}"
        deferred = @$q.defer()

        @$http.post("/balanceLess/#{account}", balance)
        .success((data, status, headers) =>
                @$log.info("Successfully deleted Balance - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to delete balance - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    updateBalance: (account, balance) ->
      @$log.debug "updateBalance #{angular.toJson(balance, true)}"
      deferred = @$q.defer()

      @$http.put("/balance/#{account}", balance)
      .success((data, status, headers) =>
              @$log.info("Successfully updated Balance - status #{status}")
              deferred.resolve(data)
            )
      .error((data, status, header) =>
              @$log.error("Failed to update balance - status #{status}")
              deferred.reject(data)
            )
      deferred.promise

servicesModule.service('BalanceService', ['$log', '$http', '$q', BalanceService])
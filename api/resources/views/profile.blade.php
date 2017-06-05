@extends('layouts.app')

@section('content')
    <div class="container">
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <div class="panel panel-default">
                    <div class="panel-heading">Add Profile</div>

                    <div class="panel-body">
                        @if (count($errors) > 0)
                            <div class="alert alert-danger">
                                <ul>
                                    @foreach ($errors->all() as $error)
                                        <li>{{ $error }}</li>
                                    @endforeach
                                </ul>
                            </div>
                        @endif

                        @if (session('success'))
                            <div class="alert alert-success">
                                {{ session('success') }}
                            </div>
                        @endif

                        <form action="/api/notification/register_profile" method="POST">
                            {{ csrf_field() }}
                            <div class="form-group">
                                <label for="exampleInputEmail1">First name</label>
                                <input type="text" class="form-control" id="firstNameInput" name="first_name" placeholder="Enter first name">
                            </div>
                            <div class="form-group">
                                <label for="exampleInputPassword1">Last name</label>
                                <input type="text" class="form-control" id="lastNameInput" name="last_name" placeholder="Enter last name">
                            </div>
                            <div class="form-group">
                                <label for="exampleInputPassword1">Birth date</label>
                                <input type="text" class="form-control" id="birthDateInput" name="birth_date" value= {{ \Carbon\Carbon::now() }}>
                            </div>
                            <div class="form-group">
                                <label for="exampleInputPassword1">Notification token</label>
                                <input type="text" class="form-control" id="notificationTokenInput" name="notification_token" placeholder="Enter notification token">
                            </div>
                            <button type="submit" class="btn btn-primary">Add Profile</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection
